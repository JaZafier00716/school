// ***********************************************************************
//
// Demo program for education in subject
// Computer Architectures and Parallel Systems.
// Petr Olivka, dep. of Computer Science, FEI, VSB-TU Ostrava, 2020/11
// email:petr.olivka@vsb.cz
//
// Example of CUDA Technology Usage with unified memory.
//
// Image transformation from RGB to BW schema. 
// Image manipulation is performed by OpenCV library. 
//
// ***********************************************************************

#include <stdio.h>
#include <cuda_device_runtime_api.h>
#include <cuda_runtime.h>
#include <opencv2/opencv.hpp>

#include "uni_mem_allocator.h"
#include "cuda_img.h"

namespace cv {
}

// Function prototype from .cu file
void cu_run_grayscale( CudaImg t_bgr_cuda_img, CudaImg t_bw_cuda_img );
void cu_insert_image( CudaImg &t_cuda_big_img, CudaImg &t_cuda_small_img, int2 t_pos, uchar3 mask);
void cu_swap_image(CudaImg &t_cuda_img1, CudaImg &t_cuda_img2);
void cu_swap2_image(CudaImg &t_cuda_img1, CudaImg &t_cuda_img2);

int main( int t_numarg, char **t_arg )
{
    // Uniform Memory allocator for Mat
    UniformAllocator allocator;
    cv::Mat::setDefaultAllocator( &allocator );

    if ( t_numarg < 3 )
    {
        printf( "Enter 2 picture filenames!\n" );
        return 1;
    }

    // Load image
    cv::Mat large_img = cv::imread( t_arg[ 1 ], cv::IMREAD_COLOR ); // CV_LOAD_IMAGE_COLOR );
    
    if ( !large_img.data )
    {
        printf( "Unable to read file '%s'\n", t_arg[ 1 ] );
        return 1;
    }

    cv::Mat small_img = cv::imread( t_arg[ 2 ], cv::IMREAD_COLOR );

    if ( !small_img.data )
    {
        printf( "Unable to read file '%s'\n", t_arg[ 1 ] );
        return 1;
    }

    // data for CUDA
    CudaImg l_large_cuda_img(large_img.size().height, large_img.size().width, ( uchar3 * ) large_img.data);
    CudaImg l_small_cuda_img(small_img.size().height, small_img.size().width, ( uchar3 * ) small_img.data);

    // Function calling from .cu file
    // // cu_run_grayscale( l_bgr_cuda_img, l_bw_cuda_img );
    // cu_insert_image( l_large_cuda_img, l_small_cuda_img, {100, 100}, {1,1,1});
    // // cv::imshow( "normal", large_img );
    // cu_insert_image( l_large_cuda_img, l_small_cuda_img, {100+(int)l_small_cuda_img.m_size.x, 100}, {0,0,1});
    // // cv::imshow( "red", large_img );
    // cu_insert_image( l_large_cuda_img, l_small_cuda_img, {100, 100+(int)l_small_cuda_img.m_size.y}, {0,1,0});
    // // cv::imshow( "green", large_img );
    // cu_insert_image( l_large_cuda_img, l_small_cuda_img, {100+(int)l_small_cuda_img.m_size.x, 100+(int)l_small_cuda_img.m_size.y}, {1,0,0});
    // // cv::imshow( "blue", large_img );

    cv::imshow( "insert all", large_img );
    cu_swap2_image(l_large_cuda_img, l_small_cuda_img);
    cv::imshow( "After", large_img );

    // Show the Color and BW image
    // cv::imshow( "Color", l_bw_cv_img );
    cv::waitKey( 0 );

    while(1);
}

